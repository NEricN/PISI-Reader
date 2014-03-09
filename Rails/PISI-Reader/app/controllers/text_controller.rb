class TextController < ApplicationController

	def upload
		text = params[:uploaded_text]

		render :text => "B"
	end
end
