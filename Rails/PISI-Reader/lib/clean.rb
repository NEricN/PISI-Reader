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