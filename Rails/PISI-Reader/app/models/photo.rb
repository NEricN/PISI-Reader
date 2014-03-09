class Photo < ActiveRecord::Base
  has_attached_file :pic, :styles => { :medium => "300x300>", :thumb => "100x100>" }, :default_url => "/images/:style/missing.png", :url  => "/assets/:id/:style/:basename.:extension", :path => ":rails_root/public/assets/:id/:style/:basename.:extension"
  validates_attachment_content_type :pic, :content_type => /\Aimage\/.*\Z/
end
