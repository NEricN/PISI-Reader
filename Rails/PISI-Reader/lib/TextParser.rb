class TextParser
	def initialize(string)
		@text = string
		@words = @text.split(/\b+/)
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
		@words_uniqueness_ratio = @words.uniq.size.to_f/@words.size
		@words_uniqueness_count = @words.uniq.size
	end

	def proper_nouns_count()
		@words.uniq.select{ |word|
			word[/[A-Z][a-z]+/]
			}.size
	end

	def proper_nouns_distances()
		@proper_nouns_indices = @words.each_index.select{ |word|
			@words[word] =~ /[A-Z][a-z]+/ }

		@pronouns_indices = @words.each_index.select{ |word|
			@words[word] =~ /he|she/ }

		@pronouns_count = @pronouns_indices.size

		@pronouns_distances = []

		@proper_nouns_indices.each_with_index do |proper, i|
			@pronouns_indices.each do |pronoun, j|
				if i + 1 < @proper_nouns_indicies.size && @proper_nouns_indicies[i] < pronoun
					break
				end
				if pronoun > proper
					@pronouns_distances.push(pronoun - proper)
				end
			end
		end
	end
end