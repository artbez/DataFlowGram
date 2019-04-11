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

class ExecutionService(connector_pb2_grpc.ExecutorServicer):
    def Execute(self, request, context):
        arg_string = ",".join(request.args)
        res = eval('myc.' + request.function + '(' + arg_string + ')')
        return connector_pb2.ExecutionResult(message=res)

    def UpdateLib(self, request, context):
        globals()['myc'] = __import__('all')
        return connector_pb2.Updated()


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
