Items = Item*

Item
	= 	_ p:ProjectTask
    	_ s:StartDate e:EndDate?
        _ t:CommaSeparated
        _ m:AllMeta
        _
        {
        	return {title: p, start: s, end: e, teams: t, meta: m}
        }

ProjectTask
	= a:NoComma _ "," _ b:NoComma { return {project: a, task: b} }

/* Meta */
AllMeta
    = m:(Meta)* { return {
        links: m.filter(i => i.link != undefined).map(i => i.link),
        labels: m.filter(i => i.labels != undefined).flatMap(i => i.labels),
        desc: m.filter(i => i.desc != undefined).map(i => i.desc).join("\n")
    } }

Meta
	= _ m: (Labels / DescLine / Link) { return m }


Labels
	= "@" _ labels:CommaSeparated { return {labels: labels} }

DescLine
	= ">" _ desc: FullLine { return {desc: desc } }

Link
	= "^" _ name: ([^|]+) "|" url: FullLine { return {link: {
    	name: name.join("").trim(),
        url: url} } }

/* Dates */

EndDate
	= _ "," _ date:(DateAndDelta / Date / Delta) { return date}

StartDate
	= (DateAndDelta / Date)

DateAndDelta
	= date: Date _ "+" _ delta:Delta { date.delta = delta.delta; return date  }

Delta
	= ([0-9]+[a-z]i) { return {delta: text().trim()} }

Date
	= [0-9]+"-"[0-9]+"-"[0-9]+ { return { date: text().trim() } }

/* Utils */

CommaSeparated
	= t:(NoComma [,]*)+ { return t.map(i => i[0].trim()) }

FullLine
	= [^\n]+ { return text().trim() }

NoComma
	= [^,\n]+ { return text().trim() }

_ "whitespace"
  = [ \t\n\r]*

