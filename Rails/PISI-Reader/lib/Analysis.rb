require './Sentence'
require './TextParser'

#values["proper nouns"] += text.proper_nouns
#sentence_values["ending forms"] += sentence.ending_forms
#sentence_values["syllables"] += sentence.syllables
#sentence_values["irreg ending"] += sentence.irreg_ending
#

texts = []
group = []
#(Dir.entries("A_I")[2..Dir.entries("A_I").length]).each do |file|
#   file = "A_I/" + file
#   group.push(file)
#end
#texts.push(group)
group = []
(Dir.entries("Z")[2..Dir.entries("Z").length]).each do |file|
   file = "Z/" + file
   group.push(file)
end
texts.push(group)
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
            sentence_values["non trivial length words"] += sentence.non_trivial_length_words
            sentence_values["said ratio"] += sentence.said_ratio
            sentence_values["average word length"] += sentence.average_word_length
            sentence_values["average word non trivial"] += sentence.average_word_non_trivial
            sentence_values["longest word length"] += sentence.longest_word_length
            sentence_values["shortest word length"] += sentence.shortest_word_length
            sentence_values["contractions length"] += sentence.contractions_length
            sentence_values["uniqueness ratio"] += sentence.uniqueness_ratio
            sentence_values["uniqueness count"] += sentence.uniqueness_count
            sentence_values["syllables avg"] += sentence.syllables_avg
            sentence_values["syllables avg non trivial"] += sentence.syllables_avg_non_trivial
            sentence_values["difficult"] += sentence.difficult
            i += 1.0
        end
        i -= 1.0
        values["sentence length char"] += sentence_values["sentence length char"]/i
        values["sentence length words"] += sentence_values["sentence length words"]/i
        values["non trivial length words"] += sentence_values["non trivial length words"]/i
        values["said ratio"] += sentence_values["said ratio"]/i
        values["average word length"] += sentence_values["average word length"]/i
        values["average word non trivial"] += sentence_values["average word non trivial"]/i
        values["longest word length"] += sentence_values["longest word length"]/i
        values["shortest word length"] += sentence_values["shortest word length"]/i
        values["contractions length"] += sentence_values["contractions length"]/i
        values["uniqueness ratio"] += sentence_values["uniqueness ratio"]/i
        values["uniqueness count"] += sentence_values["uniqueness count"]/i
        values["syllables avg"] += sentence_values["syllables avg"]/i
        values["syllables avg non trivial"] += sentence_values["syllables avg non trivial"]/i
        values["difficult"] += sentence_values["difficult"]/i
        total += 1.0
        f.close
    end
    total -= 1.0
    (values.keys).each do |key|
        values[key] /= total
    end
    values
end

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
