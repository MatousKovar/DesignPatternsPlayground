package cz.cvut.fit.niadp.mvcgame.view;

import cz.cvut.fit.niadp.mvcgame.bridge.IGameGraphics;

public class PauseView implements IView{
        private IGameGraphics gameGraphics;

        public PauseView(IGameGraphics gameGraphics){
            this.gameGraphics = gameGraphics;
        }
        
        public void render(){
            //TODO
        };
}
