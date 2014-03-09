class Sentence
	def initialize(string)
		@words = method() #assigns @words to 2
    end

	def method
        2
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
    def average_word_length
        total = 0
        (@words).each do |word|
            total += word.length
        end
        total / @words.length
    end
    def longest_word
        longest = 0
        (@words).each do |word|
            if word.length > longest
                longest = word.length
            end
        end
        longest
    end
    def shortest_word
        shortest = 1000
        (@words).each do |word|
            if word.length < shortest
                shortest = word.length
            end
        end
        shortest
    end
    def contractions
        count = 0
        (@words).each do |word|
            if /.+'.+/.match(word)
                count += 1
            end
        end
        count
    end
end
