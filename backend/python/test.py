import sys
from typing import Generator

x = []

# class Logger(object):
#
#     def __init__(self, gen):
#         self.terminal = sys.stdout
#         self.gen = gen
#         self.printed = []
#
#     def __next__(self):
#         return_value = self.a
#         self.a, self.b = self.b, self.a + self.b
#         return return_value
#
#     def __iter__(self):
#         return self
#
#
#     def write(self, message):
#         self.terminal.write("123" + message)
#         self.printed.append(message)
#
#
#     def flush(self):
#         pass

class Fib(Generator):
    def __init__(self):
        self.terminal = sys.stdout
        self.printed = []

    def send(self, ignored_arg):
        if len(self.printed) != 0:
            return_value = self.printed[0]
            self.printed = self.printed[1:]
            return return_value

    def write(self, message):
        self.terminal.write("123" + message)
        self.printed.append(message)


    def flush(self):
        pass

    def throw(self, type=None, value=None, traceback=None):
        raise StopIteration

from collections.abc import Iterator, Generator, ByteString
import unittest

class Test(unittest.TestCase):
    def test_Fib(self):
        f = Fib()
        sys.stdout = f
        #print("1")
        self.assertEqual(next(f), "1")
        # self.assertEqual(next(f), 1)
        # self.assertEqual(next(f), 1)
        # self.assertEqual(next(f), 2) #etc...
    def test_Fib_is_iterator(self):
        f = Fib()
        self.assertIsInstance(f, Iterator)
    def test_Fib_is_generator(self):
        f = Fib()
        self.assertIsInstance(f, Generator)

if __name__ == '__main__':
    unittest.main()
