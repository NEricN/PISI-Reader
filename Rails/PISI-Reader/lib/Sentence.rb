class Sentence
	def initialize(string)
		@sentence = string
		@words = split_sentence()

		@sentence_length_char = @sentence.size
		@sentence_length_words = words.length

		@irreg_ending = ending_punct()
		@nonwords = nonwords_count()
		@said_ratio = said_ratio_count()
		syllables()
	end

	def split_sentence()
		@sentence.split(/[[, ] ]/)
	end

	def syllables()
		@syllables = []
		@syllables_sum = 0
		@syllable_max = 0
		@syllable_min = 999
		@words.each do |word|
			syllables = syllable_count(word)
			@syllables.push(syllables)
			@syllables_sum += syllables
			@syllable_max = syllables > @syllable_max ? syllables : @syllable_max
			@syllable_min = syllables < @syllable_min ? syllables : @syllable_min
		end
		@syllables_avg = @syllables_sum.to_f/@words.size
	end

	def syllable_count(word)
		word.downcase!
		return 1 if word.length <= 3
		word.sub!(/(?:[^laeiouy]es|ed|[^laeiouy]e)$/, '')
		word.sub!(/^y/, '')
		word.scan(/[aeiouy]{1,2}/).size
	end

	def nonwords_count()
		temp_sentence = @sentence
		temp_sentence.sub!(/[^()"-':\/\\\\.]|/, '')
		temp_sentence.sub!(/\.\.\./, '.')
		temp_sentence.size
	end

	def ending_punct()
		@sentence.scan(/[!?]/).size > 0
	end

	def said_ratio_count()
		@sentence.scan(/" said/).size.to_f/@sentence.scan(/"[^"]+"/).size
	end
end