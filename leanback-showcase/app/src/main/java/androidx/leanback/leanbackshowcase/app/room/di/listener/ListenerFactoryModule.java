/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.leanback.leanbackshowcase.app.room.di.listener;

import androidx.leanback.leanbackshowcase.app.room.di.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

// may be removed for this version (subcomponent)

// one of the key todo can be resolved here.
// the reson bind here can cause a cycle in dependency injection is
// the binds ask you to get parameter prepared, so you have to prepare the parameter ListenerFactory
// then you can return it (but remember you are a module, the only reason you try to return something)
// is because you want to put it into a component, so component (injector) can return it
// so you have to prepare something (ListenerFactory) then you return it (ListenerFactory) dagger get confused
// *since your expression is to explicity*
// this is why when we use @Binds, we use concrete implmenetataion in parameter, and return interface
@Module
public abstract class ListenerFactoryModule {

  @PerFragment
  @Binds
  public abstract ListenerFactory bindListenerFactory(ListenerFactory module);
}
