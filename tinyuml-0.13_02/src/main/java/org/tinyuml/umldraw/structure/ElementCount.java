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
package org.tinyuml.umldraw.structure;

import java.io.Serializable;
import java.util.List;
import org.tinyuml.draw.AbstractCompositeNode;
import org.tinyuml.draw.Connection;
import org.tinyuml.draw.DiagramElement;
import org.tinyuml.model.UmlClass;
import org.tinyuml.model.UmlComponent;
import org.tinyuml.model.UmlModelElement;
import org.tinyuml.model.UmlPackage;
import org.tinyuml.umldraw.shared.NoteElement;
import org.tinyuml.umldraw.shared.UmlDiagramElement;

/**
 * An immutable count of diagram elements grouped by category. Connections
 * are not considered elements for this count.
 *
 * @author TinyUML customization
 * @version 1.0
 */
public final class ElementCount implements Serializable {

  private static final long serialVersionUID = 1L;

  private int packageCount;
  private int classCount;
  private int componentCount;
  private int noteCount;

  /**
   * Private constructor, use the count() factory method.
   */
  private ElementCount() { }

  /**
   * Counts the specified elements, including the children of composite
   * elements such as packages.
   * @param elements the elements to count
   * @return the resulting count
   */
  public static ElementCount count(List<? extends DiagramElement> elements) {
    ElementCount result = new ElementCount();
    result.countElements(elements);
    return result;
  }

  /**
   * Counts the specified elements and recursively their nested children.
   * @param elements the elements to count
   */
  private void countElements(List<? extends DiagramElement> elements) {
    for (DiagramElement element : elements) {
      if (element instanceof Connection) {
        continue;
      }
      countElement(element);
      if (element instanceof AbstractCompositeNode) {
        countElements(((AbstractCompositeNode) element).getChildren());
      }
    }
  }

  /**
   * Counts a single element according to its category.
   * @param element the element to count
   */
  private void countElement(DiagramElement element) {
    if (element instanceof NoteElement) {
      noteCount++;
    } else if (element instanceof UmlDiagramElement) {
      UmlModelElement modelElement =
        ((UmlDiagramElement) element).getModelElement();
      if (modelElement instanceof UmlPackage) {
        packageCount++;
      } else if (modelElement instanceof UmlClass) {
        classCount++;
      } else if (modelElement instanceof UmlComponent) {
        componentCount++;
      }
    }
  }

  /**
   * Returns the number of package elements.
   * @return the package count
   */
  public int getPackageCount() { return packageCount; }

  /**
   * Returns the number of class elements.
   * @return the class count
   */
  public int getClassCount() { return classCount; }

  /**
   * Returns the number of component elements.
   * @return the component count
   */
  public int getComponentCount() { return componentCount; }

  /**
   * Returns the number of note elements.
   * @return the note count
   */
  public int getNoteCount() { return noteCount; }

  /**
   * Returns the total number of elements.
   * @return the total count
   */
  public int getTotal() {
    return packageCount + classCount + componentCount + noteCount;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return String.format(
      "Total Items: %02d | Package: %02d | Class: %02d | "
      + "Component: %02d | Note: %02d",
      getTotal(), packageCount, classCount, componentCount, noteCount);
  }
}
