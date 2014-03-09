class PictureController < ApplicationController

  skip_before_filter :verify_authenticity_token

  def index
  end

  def upload
    @picture = Picture.new
    @picture.pic = params[:uploaded_file] if params[:uploaded_file].present?
    @picture.save if @picture.valid?
    render :text=>"A"
  end

  def test
    render :text=>"A"
  end
end
