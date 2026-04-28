export interface ValueRange {
  min: number
  average: number
  max: number
}

export interface Attribute {
  name: string
  description: string
  value: number
  groupName?: string
}

export interface SkillVerb {
  name: string
  description: string
}

export interface SkillDomain {
  name: string
  description: string
}

export interface Cheat {
  name: string
  description: string
}

export interface RulesetData {
  valueRange: ValueRange
  attributes: Attribute[]
  skills: SkillVerb[]
  skillDomains: SkillDomain[]
  cheats: Cheat[]
}

export interface RollResult {
  dice: number[]
  success: boolean
  paschValue: number | null
  paschCount: number | null
}

// Interne Darstellung des API-Formats mit Gruppen
interface AttributeGroupApiDto {
  group: string
  attributes: Attribute[]
}

interface RulesetApiDto {
  valueRange: ValueRange
  attributeGroups: AttributeGroupApiDto[]
  skills: SkillVerb[]
  skillDomains: SkillDomain[]
  cheats: Cheat[]
}

function fromApiDto(dto: RulesetApiDto): RulesetData {
  return {
    valueRange: dto.valueRange,
    attributes: dto.attributeGroups.flatMap(g =>
      g.attributes.map(a => ({ ...a, groupName: g.group }))
    ),
    skills: dto.skills,
    skillDomains: dto.skillDomains ?? [],
    cheats: dto.cheats ?? [],
  }
}

function toApiDto(data: RulesetData): RulesetApiDto {
  const groupMap = new Map<string, Attribute[]>()
  for (const attr of data.attributes) {
    const key = attr.groupName ?? 'Allgemein'
    if (!groupMap.has(key)) groupMap.set(key, [])
    groupMap.get(key)!.push(attr)
  }
  return {
    valueRange: data.valueRange,
    attributeGroups: Array.from(groupMap.entries()).map(([group, attributes]) => ({
      group,
      attributes: attributes.map(({ groupName: _g, ...rest }) => rest),
    })),
    skills: data.skills,
    skillDomains: data.skillDomains,
    cheats: data.cheats,
  }
}

const BASE = '/api/rulesets'

async function checkOk(res: Response, msg: string): Promise<void> {
  if (!res.ok) {
    const body = await res.text().catch(() => '')
    throw new Error(`${msg} (${res.status}${body ? ': ' + body : ''})`)
  }
}

export async function importRuleset(xml: string): Promise<RulesetData> {
  const res = await fetch(`${BASE}/import`, {
    method: 'POST',
    headers: { 'Content-Type': 'text/xml' },
    body: xml,
  })
  await checkOk(res, 'XML konnte nicht geladen werden')
  return fromApiDto(await res.json())
}

export async function saveRuleset(name: string, data: RulesetData): Promise<void> {
  const res = await fetch(`${BASE}/${encodeURIComponent(name)}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(toApiDto(data)),
  })
  await checkOk(res, 'Regelwerk konnte nicht gespeichert werden')
}

export async function rollDice(value: number): Promise<RollResult> {
  const res = await fetch('/api/roll', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ value }),
  })
  await checkOk(res, 'Würfeln fehlgeschlagen')
  return res.json()
}

export async function exportRuleset(data: RulesetData): Promise<string> {
  const res = await fetch(`${BASE}/export`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(toApiDto(data)),
  })
  await checkOk(res, 'XML konnte nicht serialisiert werden')
  return res.text()
}
