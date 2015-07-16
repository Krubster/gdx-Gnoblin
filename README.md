# gdx-Gnoblin
Gnoblin Engine is a 3D(2D is optional) game engine based on libgdx library

#For Developers
I may say - this is my training work through learning libgdx and game programming, and, if you really interested in developing and contributing this engine - you're welcome! If engine gets some popularity, I might join you and continue to work out this engine.

#Setting up

###IntelliJ users:

  Repository contains project files, so, if you have IntelliJIDEA, you just can import these files as existing Intellij project to the workspace.
 
###Eclipse users(Manual importing):

 + 1) Create new project for core(If you want do debug engine when editing it - Create second project for desktop files)
 
 + 2) Import files from core/src(And files from desktop/src for debugging - optional)
 
 + 3) Link requred libraries to core and desktop projects(located in lib directory)
 
 + 4) Res directory also required for proper engine running, it contains standart font.
 
 + 5) If you set up desktop project - link core project as dependency to it.

#Version: beta

#Status: semi-normal

#Features
- Terrain building, multitexturing, saving
- Shadow mapping from multiple directional lights(Might work with others)
- Component system(Such as character controller, 3d/2d Rigidbody, Sound Emmitter/Listener, Mesh/Sprite Renderer, Camera etc.)
- Terrain collision
- Both 2D and 3D support(Physics and graphics) - 2d is WIP
- 3D Sound
- File logging
- Flexible, low-level network
- Frustrum culling
- Maybe something else, I don't remeber
