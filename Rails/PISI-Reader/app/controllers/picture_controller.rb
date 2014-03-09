class PictureController < ApplicationController

  skip_before_filter :verify_authenticity_token

  def index
    @photos = Photo.all
  end

  def upload
    @photo = Photo.new
    @photo.pic = params[:uploaded_file] if params[:uploaded_file].present?
    @photo.save if @photo.valid?

    puts "Photo Start >>>>>>>>>>>>>>>>>>>"
    puts @photo.pic.path
    image = RTesseract.new(@photo.pic.path)
    puts image
    ocr = image.to_s
    @photo.ocr = ocr
    @photo.save if @photo.valid?
    puts "Photo End <<<<<<<<<<<<<<<<<<<<<"

    # e = Tesseract::Engine.new do |e|
    #     e.language  = :eng
    #     e.blacklist = '|'
    # end
    
    # render :text => e.text_for(@photo.pic.path)
    render :text=>"B"
  end

  def test
    render :text=>"A"
  end
end
