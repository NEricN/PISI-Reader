require 'sentiment'
require 'sentence'
require 'math_analysis'
class TextParser
	attr_accessor :sentences, :sentiments, :words_uniqueness_count, :words_uniqueness_ratio, :proper_distances_avg_avg
	def initialize(string)
		@text = string
		@words = @text.split(/\W+/)
		@sentences_array = @text.split(/[.!?] |[.!?]$/)
		@sentences = []
		@sentences_array.each do |sent|
			@sentences.push(Sentence.new(sent))
		end

		@sentiments_hash = Sentiments.new(File.join(Rails.root.join, 'lib', 'sentiments.txt')).sent_hash

		proper_nouns_find()
		proper_nouns_count()
		proper_nouns_distances_from()
		proper_nouns_distances()
		words_variety_count()
        @sentiments = negativity(@sentiments_hash)
	end

	def analyze()
	    values = {}
	    values.default = 0.0
	    values["sentiments"] += @sentiments
	    values["words uniqueness count"] += @words_uniqueness_count
	    sentence_values = {}
	    sentence_values.default = 0.0
	    i = 0.0
	    @sentences.each do |sentence|
	        sentence_values["sentence length words"] += sentence.sentence_length_words
	        sentence_values["average word non trivial"] += sentence.average_word_non_trivial
	        sentence_values["longest word length"] += sentence.longest_word_length
	        sentence_values["difficult"] += sentence.difficult
	        i += 1.0
	    end
	    values["sentence length words"] += sentence_values["sentence length words"]/i
	    values["average word non trivial"] += sentence_values["average word non trivial"]/i
	    values["longest word length"] += sentence_values["longest word length"]/i
	    values["difficult"] += sentence_values["difficult"]/i
	    
	    score = 0
	    score += 5 * (values["difficult"] - 5.6) / 4.0
	    score += 4 * (values["sentence length words"] - 8) / 5.0
	    score += 3 * (values["longest word length"] - 6.7) / 0.8
	    score -= 2 * (values["sentiments"] - 1) / 1.4
	    score += 1 * (values["average word non trivial"] - 4)/ 0.1
	    score += 1 * (values["words uniqueness count"]) / 100.0
	    score = (score / 16.0 + 1)*5

	    if score > 8
	        return 'X-Z'
	    elsif score > 4
	        return 'J-W'
	    else
	        return 'A-I'
		end
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
			word[/[A-Z][a-z]+/] and @sentiments_hash.has_key? word.downcase
			}

	end

	def proper_nouns_distances_from()
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

		if @pronouns_indices and @proper_nouns_indicies
			@proper_nouns_indices.each_with_index do |proper, i|
				@pronouns_indices.each do |pronoun, j|
					if i + 1 < @proper_nouns_indicies.size && @proper_nouns_indicies[i] < pronoun #FIX PLOX
						break
					end
					if pronoun > proper
						@pronouns_distances.push(pronoun - proper)
					end
				end
			end
		end
	end

    def negativity(sentiment_hash) #Sentiments.new("sentiments.txt").sent_hash
        total = 0
        @words.each do |word|
        	if sentiment_hash.has_key? word
        		total += sentiment_hash[word]
        	end
        end
        total
    end

    def test
    	puts @text + "\n"
    	puts @words

    	puts @sentences_array[0]

    	puts @words_uniqueness_count
    	puts @words_uniqueness_ratio

    	puts @proper_nouns
    	puts @proper_distances_avg_avg
    end
end
