require './Sentence'
require './TextParser'
require './MathAnalysis'

folders = ["ABC", "DE", "FG", "HI"]
texts = {}
folders.each do |folder|
    group = []
    Dir.foreach(folder + "/") do |file|
       next if file == ".." or file == "."
       file = folder + "/" + file
       group.push(file)
    end
    texts[folder] = group
end

compare = ['Z/Z_Twain1.txt']
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
    (texts.keys).each do |key|
        groups.push(average texts[key])
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
end

print_groups texts, compare