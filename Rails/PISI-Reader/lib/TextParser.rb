class TextParser
	def initialize(string)
		@text = string
		@words = @text.split(/\b+/)
		@sentences_array = string.split(/[.!?] /)
		@sentences = []
		@sentences_array.each do |sent|
			@sentences.push(Sentence.new(sent))
		end

		proper_nouns_count()
		proper_nouns_distances_from()
		proper_nouns_distances()

		analyze()
	end

	def analyze()

	end

	def words_variety_count()
		@words_uniqueness_ratio = @words.uniq.size.to_f/@words.size
		@words_uniqueness_count = @words.uniq.size
	end

	def proper_nouns_count()
		@proper_nouns_size = @proper_nouns.size
	end

	def proper_nouns_find()	
		@proper_nouns = @words.uniq.select{ |word|
			word[/[A-Z][a-z]+/]
			}
	end

	def proper_nouns_distance_from()
		@proper_distances_avg_avg = 0
		@proper_distances_average = []
		@proper_nouns.each do |noun|
			temp_diff = 0
			identical_nouns = @words.each_index.select{ |word|
				@words[word] == noun }
			identical_nouns.each_with_index do |id_noun, i|
				if i + 1 >= identical_nouns.size
					break
				end
				temp_diff += (identical_nouns[i + 1] - id_noun)
			end
			@proper_distances_avg_avg += temp_diff.to_f/identical_nouns.size
			@proper_distances_average.push(temp_diff.to_f/identical_nouns.size)
		end
		@proper_distances_avg_avg = @proper_distances_avg_avg.to_f/@proper_nouns.size
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