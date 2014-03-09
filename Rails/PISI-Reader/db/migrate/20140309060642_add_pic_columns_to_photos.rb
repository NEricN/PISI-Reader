class AddPicColumnsToPhotos < ActiveRecord::Migration
  def self.up
    add_attachment :photos, :pic
  end
  def self.down
    remove_attachment :photos, :pic
  end
end
