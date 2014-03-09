require 'text_parser'

class TextController < ApplicationController

	skip_before_filter :verify_authenticity_token

	def upload
		text = params[:uploaded_text]

		puts text

		parser = TextParser.new(text)
		analysis = parser.analyze()

		puts analysis

		render :text => analysis
	end
end
