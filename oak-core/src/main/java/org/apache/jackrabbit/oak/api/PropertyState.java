/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.jackrabbit.oak.api;

import java.util.List;

import javax.annotation.Nonnull;

/**
 * Immutable property state. A property consists of a name and a value.
 * A value is either an atom or an array of atoms.
 *
 * <h2>Equality and hash codes</h2>
 * <p>
 * Two property states are considered equal if and only if their names and
 * values match. The {@link Object#equals(Object)} method needs to
 * be implemented so that it complies with this definition. And while
 * property states are not meant for use as hash keys, the
 * {@link Object#hashCode()} method should still be implemented according
 * to this equality contract.
 */
public interface PropertyState {

    /**
     * If a JCR node is orderable the OAK API will expose an "{@code oak:childOrder}"
     * property state indicating the order of child nodes.
     *
     * NOTE: this is an temporary solution (OAK-232) until we reach consensus (see also OAK-182)
     *
     * // TODO: Use a proper namespace for this property?
     * // TODO: Expose this a API method on the Tree interface (see http://markmail.org/message/kzt7csiz2bd5n3ww) ?
     * // TODO: Define if/how this internal property is exposed on the JCR API
     */
    String OAK_CHILD_ORDER = "childOrder";

    /**
     * @return the name of this property state
     */
    @Nonnull
    String getName();

    /**
     * Determine whether the value if an array of atoms
     * @return  {@code true} if and only if the value is an array of atoms.
     */
    boolean isArray();

    /**
     * Value of this property.
     * @return  the single value of this property.
     * @throws IllegalStateException  if {@code isArray()} is {@code true}.
     */
    @Nonnull
    @Deprecated
    CoreValue getValue();

    /**
     * Values of this property. The returned list is immutable and contains
     * all the values of this property. If this is a single-valued property,
     * then the returned list will simply contain the single value returned
     * by the {@link #getValue()} method.
     *
     * @return immutable list of the values of this property
     */
    @Nonnull
    @Deprecated
    List<CoreValue> getValues();

    /**
     * Determine the type of this property
     * @return  the type of this property
     */
    Type<?> getType();

    /**
     * Value of this property.
     * The type of the return value is determined by the target {@code type}
     * argument. If {@code type.isArray()} is true, this method returns an
     * {@code Iterable} of the {@link Type#getBaseType() base type} of
     * {@code type} containing all values of this property.
     * If the target type is not the same as the type of this property an attempt
     * is made to convert the value to the target type. If the conversion fails an
     * exception is thrown.
     * @param type target type
     * @param <T>
     * @return the value of this property
     * @throws IllegalStateException  if {@code type.isArray() == false} and
     *         {@code this.isArray() == true}. In other words, when trying to convert
     *         from an array to an atom.
     * @throws IllegalArgumentException  if {@code type} refers to an unkown type.
     * @throws NumberFormatException  if conversion to a number failed.
     * @throws UnsupportedOperationException  if conversion to boolean failed.
     */
    @Nonnull
    <T> T getValue(Type<T> type);

    /**
     * Value at the given {@code index}.
     * The type of the return value is determined by the target {@code type}
     * argument.
     * If the target type is not the same as the type of this property an attempt
     * is made to convert the value to the target type. If the conversion fails an
     * exception is thrown.
     * @param type  target type
     * @param index
     * @param <T>
     * @return the value of this property at the given {@code index}
     * @throws IndexOutOfBoundsException  if {@code index} is less than {@code 0} or
     *         greater or equals {@code count()}.
     * @throws IllegalArgumentException  if {@code type} refers to an unkown type or if
     *         {@code type.isArray()} is true.
     */
    @Nonnull
    <T> T getValue(Type<T> type, int index);

    /**
     * The size of the value of this property.
     * @return  size of the value of this property
     * @throws  IllegalStateException  if the value is an array
     */
    long size();

    /**
     * The size of the value at the given {@code index}.
     * @param index
     * @return  size of the value at the given {@code index}.
     * @throws IndexOutOfBoundsException  if {@code index} is less than {@code 0} or
     *         greater or equals {@code count()}.
     */
    long size(int index);

    /**
     * The number of values of this property. {@code 1} for atoms.
     * @return  number of values
     */
    long count();

}
