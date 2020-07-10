import com.neuronrobotics.bowlerstudio.creature.ICadGenerator;
import com.neuronrobotics.bowlerstudio.creature.CreatureLab;
import org.apache.commons.io.IOUtils;
import com.neuronrobotics.bowlerstudio.vitamins.*;
import com.neuronrobotics.sdk.addons.kinematics.AbstractLink
import com.neuronrobotics.sdk.addons.kinematics.DHLink
import com.neuronrobotics.sdk.addons.kinematics.DHParameterKinematics
import com.neuronrobotics.sdk.addons.kinematics.LinkConfiguration
import com.neuronrobotics.sdk.addons.kinematics.MobileBase
import com.neuronrobotics.sdk.addons.kinematics.math.TransformNR

import java.nio.file.Paths;

import eu.mihosoft.vrl.v3d.CSG
import eu.mihosoft.vrl.v3d.Cube
import eu.mihosoft.vrl.v3d.FileUtil;
import eu.mihosoft.vrl.v3d.Transform;
import javafx.scene.transform.Affine;
import com.neuronrobotics.bowlerstudio.physics.TransformFactory;
import com.neuronrobotics.bowlerstudio.scripting.ScriptingEngine
println "Loading STL file"
// Load an STL file from a git repo
// Loading a local file also works here

return new ICadGenerator(){
			ICadGenerator engine=ScriptingEngine.gitScriptRun(
			"https://github.com/OperationSmallKat/Luna.git", // git location of the library
			"FrontRightCad.groovy" , // file to load
			null
			)


			@Override
			public ArrayList<CSG> generateCad(DHParameterKinematics d, int linkIndex) {
				println "Rear Left "+d
				ArrayList<DHLink> dhLinks = d.getChain().getLinks()
				DHLink dh = dhLinks.get(linkIndex)

				ArrayList<CSG> allCad = engine.generateCad( d,  linkIndex);// use the other script to initiall allign the STLs

				for(int i=0;i<allCad.size();i++) {
					Affine manipulator = allCad[i].getManipulator()
				
//					//to skip links use 'continue'
					if(	(linkIndex==2 && i==0)||
						(linkIndex==0 && i==1)
					) {
						continue;// this is the foot, stays in normal orenataiton
					}
//					// rotate all the parts about x
					if(linkIndex==0 && i==0)
						allCad[i]=allCad[i].mirrorz();
					else
						allCad[i]=allCad[i].rotx(180)
					if(linkIndex==0 && i>=2 )
						allCad[i]=allCad[i].movey(13.60)
//					// add extra rotations to specific links
					if(linkIndex==2 && i!=0 ) {
						Transform move1 = TransformFactory.nrToCSG(new TransformNR(dh.DhStep(0)))// move to link rotation space
						Transform move= move1.rotz(-15)// rotate about the link axis
						allCad[i]=allCad[i].transformed(move)// apply my new rotation
								.transformed(move1.inverse());// remove the transform that moved to link space, moving part back
					}


					allCad[i].setManipulator(manipulator)
				}

				return allCad;


			}

			@Override
			public ArrayList<CSG> generateBody(MobileBase arg0) {
				// TODO Auto-generated method stub
				return null;
			}

		};
