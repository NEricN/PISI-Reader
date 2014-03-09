class PictureController < ApplicationController

  skip_before_filter :verify_authenticity_token

  def index
    @photos = Photo.all
  end

  def upload
    @photo = Photo.new
    @photo.image = params[:uploaded_file] if params[:uploaded_file].present?
    @photo.save if @photo.valid?

    #@photo = Photo.new
    #@photo.pic = params[:uploaded_file] if params[:uploaded_file].present?
    #@photo.save if @photo.valid?

    puts "Photo Start >>>>>>>>>>>>>>>>>>>"
    image = RTesseract::Mixed.new(@photo.image.current_path) do |image|
      image.area(0,0,100,100)
    end
    ocr = image.to_s
    puts "OCR: #{ocr} Valid:[#{@photo.valid?}]"
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
