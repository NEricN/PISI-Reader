require './Sentence'
require './TextParser'


#values["proper nouns"] += text.proper_nouns
#sentence_values["ending forms"] += sentence.ending_forms
#sentence_values["syllables"] += sentence.syllables
#sentence_values["irreg ending"] += sentence.irreg_ending
#

def average(group)
    values = {}
    values.default = 0.0
    total = 1.0
    (group).each do |file|
        f = File.open(file, "r")
        text = TextParser.new(f.read)
        values["sentiments"] += text.sentiments
        values["words uniqueness count"] += text.words_uniqueness_count
    	values["words uniqueness ratio"] += text.words_uniqueness_ratio
    	values["proper distances avg avg"] += text.proper_distances_avg_avg
        sentence_values = {}
        sentence_values.default = 0.0
        i = 1.0
        (text.sentences).each do |sentence|
            sentence_values["sentence length char"] += sentence.sentence_length_char
            sentence_values["sentence length words"] += sentence.sentence_length_words
            sentence_values["said ratio"] += sentence.said_ratio
            sentence_values["average word length"] += sentence.average_word_length
            sentence_values["longest word length"] += sentence.longest_word_length
            sentence_values["shortest word length"] += sentence.shortest_word_length
            sentence_values["contractions length"] += sentence.contractions_length
            sentence_values["uniqueness ratio"] += sentence.uniqueness_ratio
            sentence_values["uniqueness count"] += sentence.uniqueness_count
            sentence_values["syllables avg"] += sentence.syllables_avg
            i += 1.0
        end
        i -= 1.0
        values["sentence length char"] += sentence_values["sentence length char"]/i
        values["sentence length words"] += sentence_values["sentence length words"]/i
        values["said ratio"] += sentence_values["said ratio"]/i
        values["average word length"] += sentence_values["average word length"]/i
        values["longest word length"] += sentence_values["longest word length"]/i
        values["shortest word length"] += sentence_values["shortest word length"]/i
        values["contractions length"] += sentence_values["contractions length"]/i
        values["uniqueness ratio"] += sentence_values["uniqueness ratio"]/i
        values["uniqueness count"] += sentence_values["uniqueness count"]/i
        values["syllables avg"] += sentence_values["syllables avg"]/i
        total += 1.0
        f.close
    end
    total -= 1.0
    (values.keys).each do |key|
        values[key] /= total
    end
    values
end


texts = [['A.txt', 'B.txt', 'C.txt'], ['D.txt', 'E.txt', 'F.txt'],['G.txt', 'H.txt', 'I.txt']]
groups = []
(texts).each do |group|
    groups.push(average group)
end
(groups[0].keys).each do |key|
    line = ""
    spaces = 25 - key.length
    line += key
    line += ":"
    for i in (0..spaces)
        line += " "
    end
    (groups).each do |group|
        line += group[key].to_s
        spaces = 20 - group[key].to_s.length
        for i in (0..spaces)
            line += " "
        end
    end
    puts line
end
