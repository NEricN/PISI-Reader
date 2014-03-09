class MathAnalysis
	attr_accessor :std, :average

	def initialize(arr)
		@average = average_find(arr)
		@variance = variance_find(arr)
		@std = std_find(arr)
	end
	def average_find(array)
		sum = 0.0
		array.each do |elem|
			sum += elem
		end
		sum/array.size
	end

	def variance_find(arr)
		sum = 0.0
		arr.each do |elem|
			sum += (elem - @average)*(elem - @average)
		end
		sum/arr.size
	end

	def std_find(arr)
		Math.sqrt(@variance)
	end

	def find_std(item)
		(item - @average)/@std
	end

	def to_s
		@average.to_s
	end
end