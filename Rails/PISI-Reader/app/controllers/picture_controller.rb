class PictureController < ApplicationController

  skip_before_filter :verify_authenticity_token

  def index
    @photos = Photo.all
  end

  def upload
    @photo = Photo.new
    @photo.image = params[:uploaded_file] if params[:uploaded_file].present?
    @photo.save if @photo.valid?

    puts @photo.image.current_path

    #@photo = Photo.new
    #@photo.pic = params[:uploaded_file] if params[:uploaded_file].present?
    #@photo.save if @photo.valid?

    puts "Photo Start >>>>>>>>>>>>>>>>>>>"
    puts 'public/'+@photo.image.url[1..@photo.image.url.length-1]
    puts @photo.image.url
    image = RTesseract.new(@photo.image.url)
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
