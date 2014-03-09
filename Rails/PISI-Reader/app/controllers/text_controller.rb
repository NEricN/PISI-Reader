class TextController < ApplicationController

	def upload
		text = params[:uploaded_text].exists? params[:uploaded_text] : "This is a cow. This is a cat."

		render :text => grade_text(text)
	end
end
