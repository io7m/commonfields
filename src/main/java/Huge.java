/*
 * Copyright © 2025 Mark Raynsford <code@io7m.com> https://www.io7m.com
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
 * IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.util.UUID;

public final class Huge
{

  public static final String ATOM_URI = "http://www.w3.org/2005/Atom";

  private Huge()
  {

  }

  public static void main(
    final String[] args)
    throws Exception
  {
    final var outputDirectory =
      Paths.get(args[0]);

    final var documents =
      DocumentBuilderFactory.newInstance();

    for (var pageIndex = 0; pageIndex < 100; ++pageIndex) {
      final var builder =
        documents.newDocumentBuilder();
      final var document =
        builder.newDocument();

      final var root =
        document.createElementNS(ATOM_URI, "feed");
      final var id =
        document.createElementNS(ATOM_URI, "id");
      final var title =
        document.createElementNS(ATOM_URI, "title");
      final var updated =
        document.createElementNS(ATOM_URI, "updated");

      id.setTextContent(
        UUID.nameUUIDFromBytes(
          "Huge-%d".formatted(pageIndex)
            .getBytes(StandardCharsets.UTF_8)
        ).toString()
      );
      title.setTextContent("Huge %d".formatted(pageIndex));
      updated.setTextContent(OffsetDateTime.now().toString());

      document.appendChild(root);
      root.appendChild(id);
      root.appendChild(title);
      root.appendChild(updated);

      if (pageIndex < 99) {
        final var next =
          document.createElementNS(ATOM_URI, "link");

        next.setAttribute("rel", "next");
        next.setAttribute("type", "application/atom+xml;type=entry;profile=opds-catalog");
        next.setAttribute("href", "huge-%d.xml".formatted(pageIndex + 1));

        root.appendChild(next);
      }

      for (var entryIndex = 0; entryIndex < 50; ++entryIndex) {
        final var entry =
          document.createElementNS(ATOM_URI, "entry");

        root.appendChild(entry);

        final var eid =
          document.createElementNS(ATOM_URI, "id");
        final var etitle =
          document.createElementNS(ATOM_URI, "title");
        final var eupdated =
          document.createElementNS(ATOM_URI, "updated");
        final var esummary =
          document.createElementNS(ATOM_URI, "summary");
        final var elink0 =
          document.createElementNS(ATOM_URI, "link");
        final var elink1 =
          document.createElementNS(ATOM_URI, "link");
        final var elink2 =
          document.createElementNS(ATOM_URI, "link");

        final var ttext =
          "Page %d Entry %d".formatted(pageIndex, entryIndex);

        eid.setTextContent(
          UUID.nameUUIDFromBytes(ttext.getBytes(StandardCharsets.UTF_8))
            .toString()
        );
        etitle.setTextContent(ttext);
        eupdated.setTextContent(OffsetDateTime.now().toString());
        esummary.setAttribute("type", "html");
        esummary.setTextContent(ttext);

        elink0.setAttribute("href", "/image/strawberries.png");
        elink0.setAttribute("type", "image/png");
        elink0.setAttribute("rel", "http://opds-spec.org/image");

        elink1.setAttribute("href", "/image/strawberriesThumb.png");
        elink1.setAttribute("type", "image/png");
        elink1.setAttribute("rel", "http://opds-spec.org/image/thumbnail");

        elink2.setAttribute("href", "/epub/charles-dickens_great-expectations.epub");
        elink2.setAttribute("type", "application/epub+zip");
        elink2.setAttribute("rel", "http://opds-spec.org/acquisition");

        entry.appendChild(eid);
        entry.appendChild(etitle);
        entry.appendChild(eupdated);
        entry.appendChild(esummary);
        entry.appendChild(elink0);
        entry.appendChild(elink1);
        entry.appendChild(elink2);
      }

      final var transformerFactory =
        TransformerFactory.newInstance();
      final var transformer =
        transformerFactory.newTransformer();

      transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

      final var outFile =
        outputDirectory.resolve("huge-%d.xml".formatted(pageIndex));
      final var result =
        new StreamResult(outFile.toFile());

      transformer.transform(
        new DOMSource(document),
        result
      );
    }
  }
}
