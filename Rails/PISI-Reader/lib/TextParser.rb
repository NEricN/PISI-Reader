class TextParser
	def initialize(string)
		@text = string
		@sentences_array = string.split(/[.!?] /)
		@sentences = []
		@sentences_array.each do |sent|
			@sentences.push(Sentence.new(sent))
		end

		analyze()
	end

	def analyze()

	end

	def words_variety_count()
		@words = @text.split(/\b+/)
		@words_uniqueness_ratio = @words.uniq.size.to_f/@words.size
		@words_uniqueness_count = @words.uniq.size
	end
end