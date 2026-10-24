/**
 * Copyright 2007 Wei-ju Wu
 *
 * This file is part of TinyUML.
 *
 * TinyUML is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * TinyUML is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with TinyUML; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package test.tinyuml.umldraw.structure;

import junit.framework.TestCase;
import org.tinyuml.model.ElementType;
import org.tinyuml.model.RelationType;
import org.tinyuml.model.UmlModelImpl;
import org.tinyuml.umldraw.structure.ClassElement;
import org.tinyuml.umldraw.structure.DiagramElementFactory;
import org.tinyuml.umldraw.structure.DiagramElementFactoryImpl;
import org.tinyuml.umldraw.structure.ElementCount;
import org.tinyuml.umldraw.structure.PackageElement;
import org.tinyuml.umldraw.structure.StructureDiagram;
import org.tinyuml.umldraw.shared.UmlConnection;
import org.tinyuml.umldraw.shared.UmlNode;

/**
 * Tests for the element count of a structure diagram.
 *
 * @author TinyUML customization
 * @version 1.0
 */
public class ElementCountTest extends TestCase {

  private StructureDiagram diagram;
  private DiagramElementFactory factory;

  /**
   * {@inheritDoc}
   */
  @Override
  protected void setUp() {
    diagram = new StructureDiagram(new UmlModelImpl());
    factory = new DiagramElementFactoryImpl(diagram);
  }

  /**
   * Returns a node of the specified type and adds it to the diagram.
   * @param type the element type
   * @return the added node
   */
  private UmlNode addNode(ElementType type) {
    UmlNode node = factory.createNode(type);
    diagram.addChild(node);
    return node;
  }

  /**
   * Tests the count of an empty diagram.
   */
  public void testEmptyDiagram() {
    ElementCount count = diagram.getElementCount();
    assertEquals(0, count.getTotal());
    assertEquals(0, count.getPackageCount());
    assertEquals(0, count.getClassCount());
    assertEquals(0, count.getComponentCount());
    assertEquals(0, count.getNoteCount());
    assertEquals("Total Items: 00 | Package: 00 | Class: 00 | "
      + "Component: 00 | Note: 00", count.toString());
  }

  /**
   * Tests that each category is counted.
   */
  public void testCountsEachCategory() {
    addNode(ElementType.PACKAGE);
    addNode(ElementType.PACKAGE);
    addNode(ElementType.CLASS);
    addNode(ElementType.CLASS);
    addNode(ElementType.COMPONENT);
    addNode(ElementType.COMPONENT);

    ElementCount count = diagram.getElementCount();
    assertEquals(6, count.getTotal());
    assertEquals(2, count.getPackageCount());
    assertEquals(2, count.getClassCount());
    assertEquals(2, count.getComponentCount());
    assertEquals(0, count.getNoteCount());
    assertEquals("Total Items: 06 | Package: 02 | Class: 02 | "
      + "Component: 02 | Note: 00", count.toString());
  }

  /**
   * Tests that connections are not counted as elements.
   */
  public void testConnectionsAreNotCounted() {
    ClassElement class1 = (ClassElement) addNode(ElementType.CLASS);
    ClassElement class2 = (ClassElement) addNode(ElementType.CLASS);
    UmlConnection connection = factory.createConnection(
      RelationType.ASSOCIATION, class1, class2);
    diagram.addChild(connection);

    ElementCount count = diagram.getElementCount();
    assertEquals(2, count.getTotal());
    assertEquals(2, count.getClassCount());
  }

  /**
   * Tests that elements nested in a package are also counted.
   */
  public void testCountsNestedElements() {
    PackageElement pkg = (PackageElement) addNode(ElementType.PACKAGE);
    UmlNode nestedClass = factory.createNode(ElementType.CLASS);
    pkg.addChild(nestedClass);

    ElementCount count = diagram.getElementCount();
    assertEquals(2, count.getTotal());
    assertEquals(1, count.getPackageCount());
    assertEquals(1, count.getClassCount());
  }

  /**
   * Tests that the count is recalculated after elements are removed.
   */
  public void testCountAfterRemoval() {
    UmlNode node = addNode(ElementType.NOTE);
    assertEquals(1, diagram.getElementCount().getNoteCount());

    diagram.removeChild(node);
    ElementCount count = diagram.getElementCount();
    assertEquals(0, count.getTotal());
    assertEquals(0, count.getNoteCount());
  }
}
