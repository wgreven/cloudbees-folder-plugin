/*
 * The MIT License
 *
 * Copyright 2013 CloudBees.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.cloudbees.hudson.plugins.folder.health;

import com.cloudbees.hudson.plugins.folder.Folder;
import com.cloudbees.hudson.plugins.folder.FolderProperty;
import hudson.DescriptorExtensionList;
import hudson.model.AbstractDescribableImpl;
import hudson.model.HealthReport;
import hudson.model.Hudson;
import hudson.model.Item;
import hudson.model.Job;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public abstract class FolderHealthMetric extends AbstractDescribableImpl<FolderHealthMetric> {

    public abstract Reporter reporter();

    public static HealthReport getHealthReport(Item item) {
        if (item instanceof Job) {
            return ((Job) item).getBuildHealth();
        }
        if (item instanceof Folder) {
            return ((Folder) item).getBuildHealth();
        }
        try {
            Method getBuildHealth = item.getClass().getMethod("getBuildHealth");
            return  (HealthReport) getBuildHealth.invoke(item);
        } catch (NoSuchMethodException e) {
            // ignore best effort only
        } catch (InvocationTargetException e) {
            // ignore best effort only
        } catch (IllegalAccessException e) {
            // ignore best effort only
        }
        return null;
    }

    public static interface Reporter {
        void observe(Item item);
        List<HealthReport> report();
    }

}
