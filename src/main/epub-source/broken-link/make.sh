#!/bin/sh -ex

EPUB_NAME="broken-link.epub"
EPUB_NAME_DOTTED="../${EPUB_NAME}"

rm -f "${EPUB_NAME}"
pushd data
zip -X -0 "${EPUB_NAME_DOTTED}" mimetype
zip -X -u "${EPUB_NAME_DOTTED}" META-INF/container.xml
zip -X -u "${EPUB_NAME_DOTTED}" epub/content.opf
zip -X -u "${EPUB_NAME_DOTTED}" epub/toc.ncx
zip -X -u "${EPUB_NAME_DOTTED}" epub/toc.xhtml
zip -X -u "${EPUB_NAME_DOTTED}" epub/title.xhtml
epubcheck "${EPUB_NAME_DOTTED}"

