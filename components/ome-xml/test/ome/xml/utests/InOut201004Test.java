//
// InOut201004Test.java
//

/*
 * ome.xml.utests
 *
 *-----------------------------------------------------------------------------
 *
 *  Copyright (C) 2007-2008 Open Microscopy Environment
 *      Massachusetts Institute of Technology,
 *      National Institutes of Health,
 *      University of Dundee,
 *      University of Wisconsin-Madison
 *
 *
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation; either
 *    version 2.1 of the License, or (at your option) any later version.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public
 *    License along with this library; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *-----------------------------------------------------------------------------
 */

package ome.xml.utests;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import static org.testng.AssertJUnit.*;

import ome.xml.r201004.Channel;
import ome.xml.r201004.Detector;
import ome.xml.r201004.Image;
import ome.xml.r201004.Instrument;
import ome.xml.r201004.Laser;
import ome.xml.r201004.MetadataOnly;
import ome.xml.r201004.OME;
import ome.xml.r201004.Pixels;
import ome.xml.r201004.Plate;
import ome.xml.r201004.Rectangle;
import ome.xml.r201004.ROI;
import ome.xml.r201004.Shape;
import ome.xml.r201004.Union;
import ome.xml.r201004.Well;
import ome.xml.r201004.enums.DimensionOrder;
import ome.xml.r201004.enums.EnumerationException;
import ome.xml.r201004.enums.LaserType;
import ome.xml.r201004.enums.NamingConvention;
import ome.xml.r201004.enums.PixelType;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * <dl><dt><b>Source code:</b></dt>
 * <dd><a href="https://skyking.microscopy.wisc.edu/trac/java/browser/trunk/components/ome-xml/test/ome/xml/utests/InOut201004Test.java">Trac</a>,
 * <a href="https://skyking.microscopy.wisc.edu/svn/java/trunk/components/ome-xml/test/ome/xml/utests/InOut201004Test.java">SVN</a></dd></dl>
 */
public class InOut201004Test {
  private static String IMAGE_ID = "Image:0";

  private static String PIXELS_ID = "Pixels:0";

  private static String INSTRUMENT_ID = "Instrument:0";

  private static String DETECTOR_ID = "Detector:0";

  private static String LIGHTSOURCE_ID = "LightSource:0";

  private static String PLATE_ID = "Plate:0";

  private static String ROI_ID = "ROI:5";

  private static DimensionOrder dimensionOrder = DimensionOrder.XYZCT;

  private static PixelType pixelType = PixelType.UINT16;

  private static final Integer SIZE_X = 512;

  private static final Integer SIZE_Y = 512;

  private static final Integer SIZE_Z = 64;

  private static final Integer SIZE_C = 3;

  private static final Integer SIZE_T = 50;

  private static final Integer WELL_ROWS = 3;

  private static final Integer WELL_COLS = 2;

  private static final NamingConvention WELL_ROW = NamingConvention.LETTER;

  private static final NamingConvention WELL_COL = NamingConvention.NUMBER;

  private static final Double RECTANGLE_X = 10.0;

  private static final Double RECTANGLE_Y = 20.0;

  private static final Double RECTANGLE_WIDTH = 128.0;

  private static final Double RECTANGLE_HEIGHT = 256.0;

  /** XML namespace. */
  public static final String XML_NS =
    "http://www.openmicroscopy.org/Schemas/OME/2010-04";

  /** XSI namespace. */
  public static final String XSI_NS =
    "http://www.w3.org/2001/XMLSchema-instance";

  /** XML schema location. */
  public static final String SCHEMA_LOCATION =
    "http://svn.openmicroscopy.org.uk/svn/specification/Xml/Working/ome.xsd";

  private Document document;

  private String asString;

  private OME ome;

  @BeforeClass
  public void setUp()
    throws ParserConfigurationException, TransformerException,
    EnumerationException
  {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder parser = factory.newDocumentBuilder();
    document = parser.newDocument();
    // Put <Image/> under <OME/>
    ome = new OME();
    ome.addImage(makeImage());
    ome.addPlate(makePlate());
    ome.addInstrument(makeInstrument());
    ome.addROI(makeROI());
    // Produce a valid OME DOM element hierarchy
    Element root = ome.asXMLElement(document);
    root.setAttribute("xmlns", XML_NS);
    root.setAttribute("xmlns:xsi", XSI_NS);
    root.setAttribute("xsi:schemaLocation",
        XML_NS + " " + SCHEMA_LOCATION);
    document.appendChild(root);
    // Produce string XML
    asString = asString();
    /* debug */ System.err.println(asString);
    // Read string XML in as a DOM tree and parse into the object hierarchy
    ome = OME.fromXMLElement(document.getDocumentElement());
  }

  @Test
  public void testValidOMENode() throws EnumerationException {
    assertNotNull(ome);
    assertEquals(1, ome.sizeOfImageList());
  }

  @Test(dependsOnMethods={"testValidOMENode"})
  public void testValidImageNode() {
    Image image = ome.getImage(0);
    assertNotNull(image);
    assertEquals(IMAGE_ID, image.getID());
  }

  @Test(dependsOnMethods={"testValidImageNode"})
  public void testValidPixelsNode() {
    Pixels pixels = ome.getImage(0).getPixels();
    assertEquals(SIZE_X, pixels.getSizeX());
    assertEquals(SIZE_Y, pixels.getSizeY());
    assertEquals(SIZE_Z, pixels.getSizeZ());
    assertEquals(SIZE_C, pixels.getSizeC());
    assertEquals(SIZE_T, pixels.getSizeT());
    assertEquals(dimensionOrder, pixels.getDimensionOrder());
    assertEquals(pixelType, pixels.getType());
    assertNotNull(pixels.getMetadataOnly());
  }

  @Test(dependsOnMethods={"testValidPixelsNode"})
  public void testValidChannelNode() {
    Pixels pixels = ome.getImage(0).getPixels();
    assertEquals(3, pixels.sizeOfChannelList());
    for (Channel channel : pixels.copyChannelList()) {
      assertNotNull(channel.getID());
    }
  }

  @Test(dependsOnMethods={"testValidOMENode"})
  public void testValidInstrumentNode() {
    Instrument instrument = ome.getInstrument(0);
    assertNotNull(instrument);
    assertEquals(INSTRUMENT_ID, instrument.getID());
  }

  @Test(dependsOnMethods={"testValidInstrumentNode"})
  public void testValidDetectorNode() {
    Detector detector = ome.getInstrument(0).getDetector(0);
    assertNotNull(detector);
    assertEquals(DETECTOR_ID, detector.getID());
  }

  @Test(dependsOnMethods={"testValidOMENode"})
  public void testValidPlateNode() {
    Plate plate = ome.getPlate(0);
    assertNotNull(plate);
    assertEquals(PLATE_ID, plate.getID());
    assertEquals(plate.getRows(), WELL_ROWS);
    assertEquals(plate.getColumns(), WELL_COLS);
    assertEquals(plate.getRowNamingConvention(), WELL_ROW);
    assertEquals(plate.getColumnNamingConvention(), WELL_COL);
    assertEquals(plate.sizeOfWellList(), WELL_ROWS * WELL_COLS);
    for (Integer row=0; row<WELL_ROWS; row++) {
      for (Integer col=0; col<WELL_COLS; col++) {
        Well well = plate.getWell(row * WELL_COLS + col);
        assertNotNull(well);
        assertEquals(well.getRow(), row);
        assertEquals(well.getColumn(), col);
      }
    }
  }

  @Test(dependsOnMethods={"testValidOMENode"})
  public void testValidROINode() {
    ROI roi = ome.getROI(0);
    assertNotNull(roi);
    assertEquals(ROI_ID, roi.getID());

    Union shapeUnion = roi.getUnion();
    assertNotNull(shapeUnion);
    Shape s = shapeUnion.getShape(0);
    assertNotNull(s);

    Rectangle rect = s.getRectangle();
    assertNotNull(rect);
    assertEquals(RECTANGLE_X, rect.getX());
    assertEquals(RECTANGLE_Y, rect.getY());
    assertEquals(RECTANGLE_WIDTH, rect.getWidth());
    assertEquals(RECTANGLE_HEIGHT, rect.getHeight());

    assertNull(s.getMask());
    assertNull(s.getEllipse());
    assertNull(s.getPoint());
    assertNull(s.getPolyline());
    assertNull(s.getLine());
    assertNull(s.getPath());
    assertNull(s.getText());
  }

  private Image makeImage() {
    // Create <Image/>
    Image image = new Image();
    image.setID(IMAGE_ID);
    // Create <Pixels/>
    Pixels pixels = new Pixels();
    pixels.setID(PIXELS_ID);
    pixels.setSizeX(SIZE_X);
    pixels.setSizeY(SIZE_Y);
    pixels.setSizeZ(SIZE_Z);
    pixels.setSizeC(SIZE_C);
    pixels.setSizeT(SIZE_T);
    pixels.setDimensionOrder(dimensionOrder);
    pixels.setType(pixelType);
    pixels.setMetadataOnly(new MetadataOnly());
    // Create <Channel/> under <Pixels/>
    for (int i = 0; i < SIZE_C; i++) {
      Channel channel = new Channel();
      channel.setID("Channel:" + i);
      pixels.addChannel(channel);
    }
    // Put <Pixels/> under <Image/>
    image.setPixels(pixels);
    return image;
  }

  private Instrument makeInstrument() {
    // Create <Instrument/>
    Instrument instrument = new Instrument();
    instrument.setID(INSTRUMENT_ID);
    // Create <Detector/> under <Instrument/>
    Detector detector = new Detector();
    detector.setID(DETECTOR_ID);
    instrument.addDetector(detector);
    // Create <Laser/> under <Instrument/>
    Laser laser = new Laser();
    //laser.setID(LIGHTSOURCE_ID);  /// XXX: Missing ID!?!
    laser.setType(LaserType.DYE);
    //instrument.addLightSource(laser);  // XXX: Fucked type hierarchy!?!
    return instrument;
  }

  private Plate makePlate() {
    Plate plate = new Plate();
    plate.setID(PLATE_ID);
    plate.setRows(WELL_ROWS);
    plate.setColumns(WELL_COLS);
    plate.setRowNamingConvention(WELL_ROW);
    plate.setColumnNamingConvention(WELL_COL);

    for (int row=0; row<WELL_ROWS; row++) {
      for (int col=0; col<WELL_COLS; col++) {
        Well well = new Well();
        well.setRow(row);
        well.setColumn(col);
        plate.addWell(well);
      }
    }

    return plate;
  }

  private ROI makeROI() {
    ROI roi = new ROI();
    roi.setID(ROI_ID);
    Union shapeUnion = new Union();
    Shape s = new Shape();
    Rectangle rect = new Rectangle();
    rect.setX(RECTANGLE_X);
    rect.setY(RECTANGLE_Y);
    rect.setWidth(RECTANGLE_WIDTH);
    rect.setHeight(RECTANGLE_HEIGHT);

    s.setRectangle(rect);
    shapeUnion.addShape(s);
    roi.setUnion(shapeUnion);

    return roi;
  }

  private String asString() throws TransformerException {
    TransformerFactory transformerFactory =
      TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();
    Source source = new DOMSource(document);
    StringWriter stringWriter = new StringWriter();
    Result result = new StreamResult(stringWriter);
    transformer.transform(source, result);
    return stringWriter.toString();
  }

  private Document fromString(String xml)
  throws ParserConfigurationException, IOException, SAXException {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    return builder.parse(new InputSource(new StringReader(xml)));
  }
}