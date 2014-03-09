class PictureController < ApplicationController

  skip_before_filter :verify_authenticity_token

  def index
  end

  def upload
    @photo = new Photo
    @photo.pic = params[:uploaded_file] if params[:uploaded_file].present?
    @photo.save if @photo.valid?
    render :text=>"A"
  end
end
