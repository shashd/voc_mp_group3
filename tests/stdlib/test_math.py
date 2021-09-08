import math
from .. utils import TranspileTestCase, BuiltinFunctionTestCase

class MathTests(TranspileTestCase):
	def test_ceil(self):
		self.assertCodeExecution("""
			import math
			x = 1.78
			print(math.ceil(x))
		""")
	def test_ceil_neg_float(self):
		self.assertCodeExecution("""
			import math
			x = -2.78
			print(math.ceil(x))
		""")
	def test_ceil_int(self):
		self.assertCodeExecution("""
			import math
			x = 34
			print(math.ceil(x))
		""")
	def test_ceil_neg_int(self):
		self.assertCodeExecution("""
			import math
			x = -2
			print(math.ceil(x))
		""")	