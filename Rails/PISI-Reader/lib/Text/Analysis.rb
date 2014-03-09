require './Sentence'
require './TextParser'
require './MathAnalysis'

texts = [['A.txt', 'B.txt', 'C.txt'], ['D.txt', 'E.txt', 'F.txt'],['G.txt', 'H.txt', 'I.txt'],['Z.txt', 'Z2.txt', 'Z3.txt']]
compare = ['A.txt']

def remove_gibberish(string)
    dictionary = []
    File.open('dictionary.txt', 'r').each do |line|
        dictionary.push(line[0, line.size - 1])
    end
    words = string.split("\s")
    real_words = []
    words.each do |word|
        if /\A[[:upper:]]+.*/.match(word)
            real_words.push(word)
        else
            dictionary.each do |real_word|
                if (word == real_word)
                    real_words.push(word)
                    break
                end
            end
        end
    end
    real_words.join(" ")
end

def grade_file(file_name)
    file = File.open(file_name, "r")
    file.close()
    grade_text(text)
end

def grade_text(textdata)
    text = TextParser.new(textdata)
    values = {}
    values.default = 0.0
    values["sentiments"] += text.sentiments
    values["words uniqueness count"] += text.words_uniqueness_count
    sentence_values = {}
    sentence_values.default = 0.0
    i = 0.0
    (text.sentences).each do |sentence|
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

def average group
    values = {}
    values["numbers"] = {}
        values["numbers"].default = 0.0
    values["sentences"] = {}

    group.each do |file|
        f = File.open(file, 'r')
        text = TextParser.new(f.read)

        text.instance_variables.each do |ivar_name|
            ivar_value = text.instance_variable_get ivar_name

            if ivar_value.class.name.to_s == "Float" or ivar_value.class.name.to_s == "Fixnum"
                values["numbers"][ivar_name.to_s] += ivar_value
            end
        end

        text.sentences.each do |sentence|
            sentence.instance_variables.each do |ivar_name|
                ivar_value = sentence.instance_variable_get ivar_name
                if ivar_value.class.name.to_s == "Float" or ivar_value.class.name.to_s == "Fixnum"
                    values["sentences"][ivar_name.to_s] = [] unless values["sentences"].has_key? ivar_name.to_s
                    values["sentences"][ivar_name.to_s].push ivar_value
                end
            end
        end
        f.close
    end

    values["numbers"].keys.each do |key|
        values["numbers"][key] /= group.size
    end

    values["sentences"].keys.each do |key|
        values["sentences"][key] = MathAnalysis.new values["sentences"][key]
    end

    values
end

def print_groups texts, compare
    comparison = average compare
    groups = []
    texts.each do |group|
        groups.push(average group)
    end

    groups[0]["numbers"].keys.each do |key|
        line = ""
        spaces = 25 - key.length
        line += key + ":"
        for i in 0..spaces
            line += " "
        end

        groups.each do |group|
            line += group["numbers"][key].to_s
            spaces = 25 - group["numbers"][key].to_s.length
            for i in 0..spaces
                line += " "
            end
        end

        line += comparison["sentences"][key].to_s

        puts line

    end

    groups[0]["sentences"].keys.each do |key|
        line = ""
        spaces = 25 - key.length
        line += key + ":"
        for i in 0..spaces
            line += " "
        end

        groups.each do |group|
            t_line = ""
            t_line += group["sentences"][key].average.round(6).to_s + "("
            t_line += group["sentences"][key].find_std(comparison["sentences"][key].average).round(2).to_s
            t_line += ")"
            spaces = 25 - t_line.length
            line += t_line
            for i in 0..spaces
                line += " "
            end
        end

        line += comparison["sentences"][key].to_s
        spaces = 25 - comparison["sentences"][key].to_s.length
        for i in 0..spaces
            line += " "
        end

        puts line

    end

    puts grade_file(compare[0])
end

print_groups texts, compare
