/**
 * Copyright 2009-2013 Oy Vaadin Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.vaadin.addon.jpacontainer.filter;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.filter.AbstractJunctionFilter;
import com.vaadin.data.util.filter.Compare;

import java.lang.reflect.Field;
import java.util.*;

/**
 */
public class InListFilter extends AbstractJunctionFilter {

    protected String propertyId;
    protected Collection values;

    public InListFilter(String propertyId, Collection values) {
        this.propertyId = propertyId;
        this.values = values;
        List<Container.Filter> f = new LinkedList<>();
        for (Object value : values) {
            f.add(new Compare.Equal(propertyId,value));
        }
        try {
            Field field = AbstractJunctionFilter.class.getDeclaredField("filters");
            field.setAccessible(true);
            field.set(this, Collections.unmodifiableCollection(f));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public String getPropertyId() {
        return propertyId;
    }

    public Collection getValues() {
        return values;
    }

    @Override
    public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
        for (Container.Filter f : getFilters()) {
            if (f.passesFilter(itemId, item)) {
                return true;
            }
        }
        return false;
    }
}
