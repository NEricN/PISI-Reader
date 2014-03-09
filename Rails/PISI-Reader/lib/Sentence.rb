class Sentence
    attr_accessor :sentence_length_char, :sentence_length_words, :irreg_ending, :said_ratio, :average_word_length,
                    :longest_word_length, :shortest_word_length, :contractions_length, :uniqueness_ratio, :uniqueness_count,
                    :syllables_avg, :non_trivial_length_words, :average_word_non_trivial, :syllables_avg_non_trivial, :difficult
	def initialize(string)
		@sentence = string
		@words = split_sentence()
        
		@sentence_length_char = @sentence.size
		@sentence_length_words = @words.length

        @non_trivial_words = filter @words
        @non_trivial_length_words = @non_trivial_words.length
        
		@irreg_ending = ending_punct()
		@nonwords = nonwords_count()
		@said_ratio = said_ratio_count()

		@ending_forms = forms()
		@average_word_length = average_word_length_find @words
        @average_word_non_trivial = average_word_length_find @non_trivial_words
		@longest_word_length = longest_word_find()
		@shortest_word_length = shortest_word_find()

		@contractions_length = contractions_find()

		@uniqueness_ratio = uniqueness_ratio_find()
		@uniqueness_count = uniqueness_count_find()
		@syllables_avg = syllables @words
        @syllables_avg_non_trivial = syllables @non_trivial_words
        
        @difficult = complex_sounds
	end

	def uniqueness_ratio_find()
		@words.uniq.size.to_f/@words.size
	end

	def uniqueness_count_find()
		@words.uniq.size
	end

	def split_sentence()
		@sentence.split(/\s+|,\s+/)
	end

	def syllables(words)
		@syllables = []
		@syllables_sum = 0
		@syllable_max = 0
		@syllable_min = 999
		words.each do |word|
			syllables = syllable_count(word)
			@syllables.push(syllables)
			@syllables_sum += syllables
			@syllable_max = syllables > @syllable_max ? syllables : @syllable_max
			@syllable_min = syllables < @syllable_min ? syllables : @syllable_min
		end
		@syllables_sum.to_f/words.size
	end

	def syllable_count(word)
		word = String.new(word)
        word.downcase!
		return 1 if word.length <= 3
		word.sub!(/(?:[^laeiouy]es|ed|[^laeiouy]e)$/, '')
		word.sub!(/^y/, '')
		word.scan(/[aeiouy]{1,2}/).size
	end

	def nonwords_count() #Bugs
		temp_sentence = String.new(@sentence)
		temp_sentence.sub!(/[^()"-':\/\\\\.]|/, '')
		temp_sentence.sub!(/\.\.\./, '.')
		temp_sentence.size
	end

	def ending_punct()
		@sentence.scan(/[!?]/).size > 0
	end

	def said_ratio_count() #Bugs
		@sentence.scan(/" said/).size.to_f/@sentence.scan(/"[^"]+"/).size
	end

    def length
        @words.length
    end

    def forms
        endings = [/.*ed\W*\z/, /.*ing\W*\z/]
        occurences = Array.new endings.length, 0
        (@words).each do |word|
            (0...endings.length).each do |i|
                if endings[i].match(word)
                    occurences[i] += 1
                end
            end
        end
        occurences
    end
    def average_word_length_find(words)
        total = 0
        (words).each do |word|
            total += word.length
        end
        total.to_f / words.length
    end
    def longest_word_find
        longest = 0
        (@words).each do |word|
            if word.length > longest
                longest = word.length
            end
        end
        longest
    end
    def shortest_word_find
        shortest = 1000
        (@words).each do |word|
            if word.length < shortest
                shortest = word.length
            end
        end
        shortest
    end
    def contractions_find
        count = 0
        (@words).each do |word|
            if /.+'.+/.match(word)
                count += 1
            end
        end
        count
    end
    def filter(words)
        non_trivial = []
        words.each do |word|
            if (word != "a" and word != "the" and word != "is" and word != "I")
                non_trivial.push(word)
            end
        end
        non_trivial
    end
    def complex_sounds
        total = 0
        complex = /[ph|pn|mn|rh|hy|eu]/
        @words.each do |word|
            if complex.match(word)
                total += 1
            end
        end
        total
    end
end
