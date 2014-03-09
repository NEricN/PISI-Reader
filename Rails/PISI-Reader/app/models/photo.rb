class Photo < ActiveRecord::Base
	mount_uploader :image, ImageUploader
  # has_attached_file :pic, :styles => { :medium => "300x300>", :thumb => "100x100>" }, :default_url => "/images/:style/missing.png", :url  => "/assets/:id/:style/:basename.:extension", :path => ":rails_root/public/assets/:id/:style/:basename.:extension"
  # validates_attachment :pic, content_type: { content_type: ['image/jpeg', 'image/png', 'image/gif'] }
end
