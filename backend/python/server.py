import queue
import threading
from concurrent import futures
import time
import logging
import os
from typing import Generator
import json

import grpc
import sys

import sys

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

    def run(self):
        self._return = eval(self.arg)

    def join(self, *args):
        threading.Thread.join(self, *args)
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
            foo = ThreadWithReturnValue('myc.' + request.function + '(' + arg_string + ')')
            foo.start()
            while foo.is_alive():
                try:
                    elem = log.get_q().get(timeout=1)
                    yield connector_pb2.ExecutionResult(msg=elem)
                except queue.Empty:
                    pass
            res = foo.join()
            sys.stdout = old_stdout

        else:
            if params['is_default_function'] == "true":
                prefix = ''
            else:
                prefix = 'myc.'

            if arg_string is None or arg_string == '':
                arg_suffix = ''
            else:
                arg_suffix = ','

            old_stdout = sys.stdout
            sys.stdout = log
            foo = ThreadWithReturnValue('myc.' + request.function + '(' + arg_string + ')')
            foo.start()
            while foo.is_alive():
                try:
                    elem = log.get_q().get(timeout=1)
                    yield connector_pb2.ExecutionResult(msg=elem)
                except queue.Empty:
                    pass
            res = foo.join()
            sys.stdout = old_stdout

        global next_id
        next_name = 'i' + str(next_id)
        next_id = next_id + 1

        global_vars[next_name] = res
        yield connector_pb2.ExecutionResult(ref=next_name)

    def UpdateLib(self, request, context):
        globals()['myc'] = __import__('all')
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
