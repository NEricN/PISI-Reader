class PictureController < ApplicationController

  skip_before_filter :verify_authenticity_token

  def index
  end

  def upload
    puts params
    render :text=>"A"
  end
end
