class PictureController < ApplicationController
  def index
  end

  def upload
    puts params
    render :text=>"A"
  end
end
