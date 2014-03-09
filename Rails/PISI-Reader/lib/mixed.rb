# encoding: UTF-8
require 'rtesseract'
class Mixed
  # Class to read an image from specified areas
  attr_reader :areas

  def initialize(src = '', options = {})
    @source  = Pathname.new src
    @options = options
    @value   = ''
    @areas = options.delete(:areas) || []
    yield self if block_given?
  end

  def area(x, y, width, height)
    @value = ''
    @areas << { :x => x,  :y => y, :width => width, :height => height }
  end

  def clear_areas
    @areas = []
  end

  # Convert parts of image to string
  def convert
    @value = ''
    @areas.each_with_object(Rtesseract.new(@source.to_s, @options.dup)) do |area, image|
      image.crop!(area[:x], area[:y], area[:width], area[:height])
      @value << image.to_s
    end
  rescue => error
    raise Rtesseract::ConversionError.new(error)
  end

  # Output value
  def to_s
    return @value if @value != ''
    if @source.file?
      convert
      @value
    else
      fail Rtesseract::ImageNotSelectedError.new(@source)
    end
  end

  # Remove spaces and break-lines
  def to_s_without_spaces
    to_s.gsub(' ', '').gsub("\n", '').gsub("\r", '')
  end
end
