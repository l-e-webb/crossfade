

class ModularMatrix:

	def __init__(self, _size, _matrix=None, _order=2):
		self.order = _order
		self.size = _size
		if _matrix:
			self.matrix = _matrix
		else:
			self.matrix =[]
			for i in range(0, self.size):
				row = []
				for j in range(0, self.size):
					row.append(0)
				self.matrix.append(row)

	def __radd__(self, other):
		if self.order != other.order or self.size != other.size:
			return None
		newMatrix = []
		for i in range(0, self.size):
			row = []
			for j in range(0, self.size):
				value = (self.matrix[i][j] + other.matrix[i][j]) % self.order
				row.append(value)
			newMatrix.append(row)
		return ModularMatrix(self.size, newMatrix, self.order)

	def __str__(self):
		string = ""
		for i in range(0, self.size):
			for j in range(0, self.size):
				string += str(self.matrix[i][j])
				if j < self.size - 1:
					string += " "
			string += "\n"
		return string


def make_basis_matrix(size, i, j, order=2):
	matrix = []
	for k in range(0,size):
		row = []
		for l in range(0,size):
			if l == j or k == i:
				row.append(1)
			else:
				row.append(0)
		matrix.append(row)
	return ModularMatrix(size, matrix, order)


def computeMatrixSum(matrixList):
	size = matrixList[0].size
	order = matrixList[0].order
	matrix = ModularMatrix(size, _order=order)
	for m in matrixList:
		matrix += m
	return matrix

def main():
	matrixList = [
		make_basis_matrix(5,0,0),
		make_basis_matrix(5,0,4),
		make_basis_matrix(5,4,0),
		make_basis_matrix(5,4,4)
	]
	print computeMatrixSum(matrixList)

main()