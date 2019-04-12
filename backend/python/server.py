from concurrent import futures
import time
import logging
import os

import grpc
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


class ExecutionService(connector_pb2_grpc.ExecutorServicer):
    def Execute(self, request, context):
        global global_vars
        parsed = list(map(lambda x: 'global_vars[\'' + x + '\']', request.args))

        arg_string = ",".join(parsed)
        params = request.params
        if params is None or params == {}:
            res = eval('myc.' + request.function + '(' + arg_string + ')')
        else:
            if params['is_default_function'] == "true":
                prefix = ''
            else:
                prefix = 'myc.'

            if arg_string is None or arg_string == '':
                arg_suffix = ''
            else:
                arg_suffix = ','
            res = eval(prefix + request.function + '(' + arg_string + arg_suffix + 'params)')

        global next_id
        next_name = 'i' + str(next_id)
        next_id = next_id + 1

        global_vars[next_name] = res
        return connector_pb2.ExecutionResult(ref=next_name)

    def UpdateLib(self, request, context):
        globals()['myc'] = __import__('all')
        return connector_pb2.Updated()

    def Out(self, request, context):
        global global_vars
        res = global_vars[request.ref]
        return connector_pb2.OutResult(json=str(res))


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
