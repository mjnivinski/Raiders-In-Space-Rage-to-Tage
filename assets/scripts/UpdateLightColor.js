var JavaPackages = new JavaImporter(
	Packages.tage.Light
);

with (JavaPackages)
{
	function updateAmbientColor(thisLight)
	{	thisLight.setAmbient(0.0, 0.0, 1.0);
	}
}
