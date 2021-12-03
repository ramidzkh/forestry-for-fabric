# Forestry

A rewrite of [ForestryMC][root] for Fabric.

## Why?

Forestry, ever since 1.12, has been lacking in terms of code quality and players. I hope that rewriting it will drive
players to experiment with a classic mod again, while also bringing new contributors. I chose Fabric because that's the
only modding platform I've seriously used, but I'm open to maintaining a Forge version of this if I ever reach a stable
state on Fabric and there is enough demand. If someone else does a Forge port of this, I'll happily link it here.

## Code style

I'm going to be pretty lax - using IntelliJ defaults are best, but I require spaces so when I inevitably reformat
everything I don't end up in the blame.

Annotate everything in the client package with `@Environment(EnvType.CLIENT)`. Follow the current Mixin structure.

## License and credits

Forestry is under the [GNU Lesser General Public License v3.0 or later][lgpl]. Forestry is a mod originally by SirSengir
and a lot of other contributors, please see [this page][contributors] for a list of people you should be thankful to!

[root]: https://github.com/ForestryMC/ForestryMC/
[lgpl]: https://spdx.org/licenses/LGPL-3.0-or-later.html
[contributors]: https://github.com/ForestryMC/ForestryMC/graphs/contributors
