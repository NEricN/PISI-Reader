class PictureController < ApplicationController

  skip_before_filter :verify_authenticity_token

  def index
  end

  def upload
    puts params.keys
    @photo = Photo.new
    @photo.pic = params[:uploaded_file] if params[:uploaded_file].present?
    @photo.save if @photo.valid?
    render :text=>"B"
  end

  def test
    render :text=>"A"
  end
end
