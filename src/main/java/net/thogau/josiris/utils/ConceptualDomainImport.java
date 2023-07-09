package net.thogau.josiris.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/*
 * chaine à rechercher 	: (.*)\t(.*)\t(.*)\t(.*)\t(.*)\t(.*)\t(.*)\t(.*)\t(.*)\t(.*)
 * remplacer par		: insert into conceptual_domain (type, version, id, value_meaning, label_value_meaning, referentiel, url, type_conceptual_domain, format_conceptual_domain, conceptual_domain) values ('$1',$2,$3,'$4','$5','$6','$7','$8','$9','$10')
 */
public class ConceptualDomainImport {

	public static void main(String[] args) {
		int i = 7457;
		try {
			Scanner scanner = new Scanner(new File("tmp.txt"));

			while (scanner.hasNextLine()) {
				System.out.println("GenomeEntityDatabase\t1\t" + i++ + "\t" + scanner.nextLine());
			}

			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
