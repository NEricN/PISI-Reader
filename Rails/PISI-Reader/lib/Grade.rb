require './Sentence'
require './TextParser'

file = File.open("A.txt", "r")

text = TextParser.new(file.read)
values = {}
values.default = 0.0
values["sentiments"] += text.sentiments
values["words uniqueness count"] += text.words_uniqueness_count
sentence_values = {}
sentence_values.default = 0.0
i = 1.0
(text.sentences).each do |sentence|
    sentence_values["sentence length words"] += sentence.sentence_length_words
    sentence_values["average word non trivial"] += sentence.average_word_non_trivial
    sentence_values["longest word length"] += sentence.longest_word_length
    sentence_values["difficult"] += sentence.difficult
    i += 1.0
end
i -= 1.0
values["sentence length words"] += sentence_values["sentence length words"]/i
values["average word non trivial"] += sentence_values["average word non trivial"]/i
values["longest word length"] += sentence_values["longest word length"]/i
values["difficult"] += sentence_values["difficult"]/i
file.close

score = 0
score += 4 * (values["sentence length words"] - 8) / 5.0
score += 3 * (values["longest word length"] - 6.7) / 0.8
score += 3 * (values["difficult"] - 5.6) / 4.0
score -= 2 * (values["sentiments"] - 1) / 1.4
score += 1 * (values["average word non trivial"] - 4)/ 0.1
score += 1 * (values["words uniqueness count"]) / 100.0
puts score / 14.0
