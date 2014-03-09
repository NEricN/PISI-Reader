class TextParser
	def initialize(string)
		@sentences_array = string.split(/[.!?] /)
		@sentences = []
		@sentences_array.each do |sent|
			@sentences.push(Sentence.new(sent))
		end

		analyze()
	end

	def analyze()

	end
end