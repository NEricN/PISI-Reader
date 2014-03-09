class Sentiments
    attr_accessor :sent_hash
    def initialize(file)
        @sent_hash = Hash.new
        File.open(file, 'r') do |f|
            while line = f.gets
                word, sent = line.split ' '
                @sent_hash[word] = sent.to_f
            end
        end
    end
end
        
