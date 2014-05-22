require './Sentence'
require './TextParser'


def analyze(file)
    f = File.open(file, "r")
    text = TextParser.new(f.read)
    values = Array.new(6, 0.0)
    #values[5] += text.sentiments
    values[4] += text.words_uniqueness_count
    sentence_values = Array.new(6, 0.0)
    i = 1.0
    (text.sentences).each do |sentence|
        sentence_values[1] += sentence.sentence_length_words
        sentence_values[3] += sentence.average_word_non_trivial
        sentence_values[2] += sentence.longest_word_length
        sentence_values[0] += sentence.difficult
        i += 1.0
    end
    i -= 1.0
    values[1] += sentence_values[1]/i
    values[3] += sentence_values[3]/i
    values[2] += sentence_values[2]/i
    values[0] += sentence_values[0]/i
    f.close
    return values
end

def total_score(a, b, c, values)
    score = 0.0
    (0..4).each do |i|
        score += ((values[i] - a[i]).abs / b[i]) ** c[i]
    end
    return score
end

def generate_values(texts)
    text_values = Hash.new()
    (texts.keys).each do |key|
        text_values[key] = []
        (texts[key]).each do |file|
            text_values[key].push(analyze file)
        end
    end
    text_values
end

def total_deviation(a, b, c)
    total = 0.0
    (@text_values.keys).each do |key|
        (@text_values[key]).each do |values|
            score = total_score a, b, c, values
            total += (@letter_values[key] - score) ** 2
        end
    end
    total
end

def replace(array, index, new_val) 
    new_array = Array.new(array)
    new_array[index] = new_val
    return new_array
end

def test(a, b, c, name, i)
    if i > 4
        if name == "a"
            test(a, b, c, "b", 0)
            return
        end
        if name == "b"
            test(a, b, c, "c", 0)
            return 
        end
        if name == "c"
            dev = total_deviation(a, b, c)
            if dev < @min_val
                @min_val = dev
                @min_a = a
                @min_b = b
                @min_c = c
            end
            return
        end
    end
    if name == "a"
        curr = a
        curr_max = @a_max
        curr_step = @a_step
        new = curr[i]
        while new < curr_max[i]
            test(replace(curr, i, new), b, c, name, i + 1)
            new *= curr_step[i]
        end
    end
    if name == "b"
        curr = b
        curr_max = @b_max
        curr_step = @b_step
        new = curr[i]
        while new < curr_max[i]
            test(a, replace(curr, i, new), c, name, i + 1)
            new *= curr_step[i]
        end
    end
    if name == "c"
        curr = c
        curr_max = @c_max
        curr_step = @c_step
        new = curr[i]
        while new.abs < curr_max[i].abs
            test(a, b, replace(curr, i, new), name, i + 1)
            new += curr_step[i]
        end
    end
end

def deviation(a, b, c, i)
    total = 0.0
    (@text_values.keys).each do |key|
        (@text_values[key]).each do |values|
            total += ((@letter_values[key] - ((values[i] - a).abs / b) ** c ).abs) ** 2
        end
    end
    total
end

def test_indv(values, max, step, i, test_ind)
    if i >= values.length
        dev = deviation(values[0], values[1], values[2], test_ind) 
        if dev < @min_val
            @min_val = dev
            @best_values = values
        end
        return
    end
    new = values[i]
    while new < max[i]
        test_indv(replace(values, i, new), max, step, i + 1, test_ind)
        new *= step[i]
    end
end

def indival(a, b, c, text)
    return total_score(a, b, c, analyze(text))
end

texts = {}
folders = ["ABC", "DE", "FG", "HI"]
#, "JKL", "MNOP", "QRST", "UV", "W", "XY", "Z"]
@letter_values = {"ABC" => 1.0, "DE" => 3.5, "FG" => 5.5, "HI" => 7.5, "JKL" => 10.0, "MNOP" => 13.5,
                    "QRST" => 17.5, "UV" => 20.5, "W" => 22.0, "XY" => 23.5, "Z" => 25.0}                   
folders.each do |folder|
    group = []
    Dir.foreach(folder + "/") do |file|
       next if file == ".." or file == "."
       file = folder + "/" + file
       group.push(file)
    end
    texts[folder] = group
end

@text_values = generate_values(texts)

@a_min = [2.0, 5.0, 0.8, 2.9, 3.4]
@a_max = [3.3, 5.3, 1.0, 3.5, 3.6]
@a_step = [1.01, 1.01, 1.01, 1.01, 1.01]

@b_min = [0.01, 0.0001, 0.5, 0.000001, 0.9]
@b_max = [0.05, 0.001, 1.0, 0.000005, 1.0]
@b_step = [1.01, 1.01, 1.01, 1.01, 1.01] 

@c_min = [0.1, 0.1, 0.7, 0.1, 0.3]
@c_max = [0.4, 0.2, 0.9, 0.3, 0.7]
@c_step = [1.01, 1.01, 1.01, 1.01, 1.01]


@a = @a_min
@b = @b_min
@c = @c_min
@min_a = @a
@min_b = @b
@min_c = @c

(0..4).each do |i|
    @min_val = Float::INFINITY
    @best_values = []
    test_indv([@a_min[i], @b_min[i], @c_min[i]], [@a_max[i], @b_max[i], @c_max[i]],
            [@a_step[i], @b_step[i], @c_step[i]], 0, i)
    puts i
    puts @best_values
end




