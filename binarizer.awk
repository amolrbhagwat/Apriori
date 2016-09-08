BEGIN{
	FS = ","
}
	
{
	lines[NR] = $0
	for(f = 1; f <= NF; f++){
		value = $f
		if(f in a){
			if(value in a[f]){
#				print "Present", f, value, a[f][value]
			}
			else{
				a[f][value] = length(a[f]) + 1
#				print "Added  ", f, value, a[f][value]
			}
		}
		else{
			a[f][value] = 1
#			print "New    ", f, value, a[f][value]
		}
	}
}

END{
	for(p in a){
		for(q in a[p]){
#			print p, q, a[p][q]
		}
	}

	for(num = 1; num <= length(lines); num++){
		split(lines[num], splits, ",")

		for(i = 1; i <= length(splits); i++){
#			print i, splits[i], a[i][splits[i]]
			
			if(a[i][splits[i]] <= length(a[i])/3 + 1){
				#print("yes ", a[i][splits[i]])
				printf("1 ")	
			}
			else{
				#print("no  ", a[i][splits[i]])
				printf("0 ")	
			}
		}
		printf("\n")
	}	
}

