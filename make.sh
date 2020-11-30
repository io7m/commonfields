#!/bin/sh

fatal()
{
  echo "fatal: $1" 1>&2
  exit 1
}

which rsync || fatal "Could not locate rsync"

rm -rf out || fatal "Could not remove output directory"

for dir in image groups
do
  mkdir -p "out/${dir}" || fatal "Could not create output directory"
done

cp src/main/css/*.css out/ || fatal "Could not copy CSS"
cp src/main/image/*.png out/image || fatal "Could not copy images"
rsync -av src/main/xml/ out/ || fatal "Could not copy feeds"
rsync -av src/main/epub out/ || fatal "Could not copy epubs"
rsync -av src/main/audiobook out/ || fatal "Could not copy audiobooks"
