import json
import logging
import os
import queue
import sys
import threading
import time
from concurrent import futures
import traceback


import grpc

import connector_pb2
import connector_pb2_grpc

_ONE_DAY_IN_SECONDS = 60 * 60 * 24

cwd = os.getcwd()
sys.path.append(cwd + '/lib/')
sys.path.append('/Users/artemii.bezguzikov/project/DataFlowGram/lib/')

global_vars = {}
next_id = 0


def init_file(params):
    return params['path']


class ThreadWithReturnValue(threading.Thread):
    def __init__(self, arg):
        threading.Thread.__init__(self)
        self.arg = arg
        self._return = None
        self._error = None

    def run(self):
        try:
            self._return = eval(self.arg)
        except:
            tb = traceback.format_exc()
            self._error = tb

    def join(self, *args):
        threading.Thread.join(self, *args)
        if self._error:
            raise Exception(self._error)
        return self._return


class ExecutionService(connector_pb2_grpc.ExecutorServicer):

    def Execute(self, request, context):
        global global_vars
        parsed = list(map(lambda x: 'global_vars[\'' + x + '\']', request.args))
        arg_string = ",".join(parsed)
        params = request.params

        q = queue.Queue()
        log = Logger(q)
        if params is None or params == {}:

            old_stdout = sys.stdout
            sys.stdout = log
            foo = ThreadWithReturnValue(self.__getLocation(request.category, request.file) + '.' + request.function + '(' + arg_string + ')')
            foo.start()
            while foo.is_alive():
                try:
                    elem = log.get_q().get(timeout=1)
                    yield connector_pb2.ExecutionResult(msg=elem)
                except queue.Empty:
                    pass
            try:
                    res = foo.join()
            except Exception as error:
                raise error
            finally:
                sys.stdout = old_stdout

        else:
            if params['is_default_function'] == "true":
                prefix = ''
            else:
                prefix = self.__getLocation(request.category, request.file) + '.'

            if arg_string is None or arg_string == '':
                arg_suffix = ''
            else:
                arg_suffix = ','

            old_stdout = sys.stdout
            sys.stdout = log
            foo = ThreadWithReturnValue(prefix + request.function + '(' + arg_string + ')')
            foo.start()
            while foo.is_alive():
                try:
                    elem = log.get_q().get(timeout=1)
                    yield connector_pb2.ExecutionResult(msg=elem)
                except queue.Empty:
                    pass

            try:
                res = foo.join()
            except Exception as error:
                raise error
            finally:
                sys.stdout = old_stdout

        global next_id
        next_name = 'i' + str(next_id)
        next_id = next_id + 1

        global_vars[next_name] = res
        yield connector_pb2.ExecutionResult(ref=next_name)

    def UpdateLib(self, request, context):
        sys.path.append(request.repo)
        for location in request.locations:
            new_path = request.repo + '/python/' + location.category
            if new_path not in sys.path:
                sys.path.append(request.repo + '/python/' + location.category)
            globals()[self.__getLocation(location.category, location.file)] = __import__(location.file)
        return connector_pb2.Updated()

    def OutData(self, request, context):
        global global_vars
        res = global_vars[request.ref]
        typedRes = str(res)
        if type == "Json":
            typedRes = json.dumps(res)

        return connector_pb2.OutResult(value=typedRes)

    def InData(self, request, context):
        global global_vars
        type = request.type
        if type == "Int":
            res = int(request.value)
        if type == "Float":
            res = float(request.value)
        if type == "String":
            res = str(request.value)
        if type == "Json":
            res = json.load(request.value)

        global next_id
        next_name = 'i' + str(next_id)
        next_id = next_id + 1

        global_vars[next_name] = res

        return connector_pb2.InResult(ref=next_name)

    @staticmethod
    def __getLocation(category, file):
        return 'myc_' + category + '_' + file


class Logger(object):
    def __init__(self, q):
        self.terminal = sys.stdout
        self.q = q

    def get_q(self):
        return self.q

    def write(self, message):
        if message.strip() != '':
            self.terminal.write(message + "\n")
            self.q.put(message)

    def flush(self):
        pass

    def throw(self, type=None, value=None, traceback=None):
        raise StopIteration


def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    connector_pb2_grpc.add_ExecutorServicer_to_server(ExecutionService(), server)
    server.add_insecure_port('[::]:50051')
    server.start()
    try:
        while True:
            time.sleep(_ONE_DAY_IN_SECONDS)
    except KeyboardInterrupt:
        server.stop(0)


if __name__ == '__main__':
    logging.basicConfig()
    serve()
